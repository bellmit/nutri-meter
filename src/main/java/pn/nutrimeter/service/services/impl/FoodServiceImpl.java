package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.Measure;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.FoodRepository;
import pn.nutrimeter.data.repositories.MeasureRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.FoodAddFailureException;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.facades.AuthenticationFacade;
import pn.nutrimeter.service.models.FoodServiceModel;
import pn.nutrimeter.service.services.api.FoodService;
import pn.nutrimeter.service.services.validation.FoodValidationService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    private final MeasureRepository measureRepository;

    private final UserRepository userRepository;

    private final AuthenticationFacade authenticationFacade;

    private final FoodValidationService foodValidationService;

    private final ModelMapper modelMapper;

    public FoodServiceImpl(UserRepository userRepository,
                           MeasureRepository measureRepository, AuthenticationFacade authenticationFacade,
                           FoodValidationService foodValidationService,
                           FoodRepository foodRepository,
                           ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.measureRepository = measureRepository;
        this.authenticationFacade = authenticationFacade;
        this.foodValidationService = foodValidationService;
        this.foodRepository = foodRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creating a food
     * @param foodServiceModel food service model (DTO)
     * @return FoodServiceModel
     */
    @Override
    public FoodServiceModel create(FoodServiceModel foodServiceModel) {
        if (!this.foodValidationService.isValid(foodServiceModel)) {
            throw new FoodAddFailureException(ErrorConstants.INVALID_FOOD_MODEL);
        }

        foodServiceModel.setKcalPerHundredGrams(this.getTotalKcal(foodServiceModel));

        Food food = this.modelMapper.map(foodServiceModel, Food.class);

        Boolean isCustom = foodServiceModel.getIsCustom();

        // SETS IT TO TRUE IF THE CHECKBOX IS CHECKED OR NULL]
        foodServiceModel.setIsCustom(isCustom == null || isCustom);

        if (food.isCustom()) {
            User user = this.getUser();
            food.setUser(user);
        }

        // THIS IS DONE AUTOMATICALLY BY THE MAPPER
//        food.setFoodCategories(foodServiceModel.getFoodCategories()
//                .stream()
//                .map(category -> this.foodCategoryRepository
//                        .findById(category.getId())
//                        .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_CATEGORY_ID)))
//                .collect(Collectors.toList()));

        List<Measure> defaultMeasures = Arrays.asList(
                this.measureRepository.findByName("g"), this.measureRepository.findByName("oz"));

        if (food.getMeasures() == null) {
            food.setMeasures(defaultMeasures);
        } else {
            food.getMeasures().addAll(defaultMeasures);
        }

        this.foodRepository.saveAndFlush(food);

        return this.modelMapper.map(food, FoodServiceModel.class);
    }

    /**
     * Getting all the foods
     * @return List<FoodServiceModel>
     */
    @Override
    public List<FoodServiceModel> getAll() {
        return this.foodRepository.findAll()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Getting all the foods from a search
     * @param specification search criteria
     * @return List<FoodServiceModel>
     */
    @Override
    public List<FoodServiceModel> getAll(Specification<Food> specification) {
        return this.foodRepository.findAll(specification)
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Getting all non custom foods
     * @return List<FoodServiceModel>
     */
    @Override
    public List<FoodServiceModel> getAllNonCustom() {
        return this.foodRepository.findAllNonCustom()
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Getting all custom foods of the currently logged in user
     * @return List<FoodServiceModel>
     */
    @Override
    public List<FoodServiceModel> getAllCustomOfUser() {
        String username = this.getUsername();

        return this.foodRepository.findAllCustomOfUser(username)
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Getting all favorite foods of the currently logged in user
     * @return List<FoodServiceModel>
     */
    @Override
    public List<FoodServiceModel> getAllFavoritesOfUser() {
        String username = this.getUsername();

        return this.foodRepository.findAllFavorites(username)
                .stream()
                .map(f -> this.modelMapper.map(f, FoodServiceModel.class))
                .collect(Collectors.toList());
    }

    /**
     * Getting a food by its ID
     * @param foodId food's ID
     * @return FoodServiceModel
     */
    @Override
    public FoodServiceModel getById(String foodId) {
        Food food = this.foodRepository.findById(foodId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_FOOD_ID));
        FoodServiceModel foodServiceModel = this.modelMapper.map(food, FoodServiceModel.class);

        User user = this.getUser();
        if (user.getFavoriteFoods().contains(food)) {
            foodServiceModel.setIsFavorite(true);
        }

        return foodServiceModel;
    }

    /**
     * Adding a food by its ID to the user's favorite list
     * @param foodId food's ID
     * @return FoodServiceModel
     */
    @Override
    public FoodServiceModel addFoodAsFavorite(String foodId) {
        Food food = this.foodRepository.findById(foodId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_FOOD_ID));
        User user = this.getUser();
        user.getFavoriteFoods().add(food);
        this.userRepository.saveAndFlush(user);

        return this.modelMapper.map(food, FoodServiceModel.class);
    }

    /**
     * Removing a food by its ID to the user's favorite list
     * @param foodId food's ID
     * @return FoodServiceModel
     */
    @Override
    public FoodServiceModel removeFoodAsFavorite(String foodId) {
        Food food = this.foodRepository.findById(foodId)
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_FOOD_ID));
        User user = this.getUser();

        if (user.getFavoriteFoods().contains(food)) {
            user.getFavoriteFoods().remove(food);
            this.userRepository.saveAndFlush(user);
        }

        return this.modelMapper.map(food, FoodServiceModel.class);
    }

    private String getUsername() {
        return this.authenticationFacade.getUsername();
    }

    private User getUser() {
        String username = this.getUsername();
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USERNAME_NOT_FOUND));
    }

    private int getTotalKcal(FoodServiceModel food) {
        double proteins = food.getTotalProteins() == null
                ? 0.0
                : food.getTotalProteins();
        double carbs = food.getTotalCarbohydrates() == null
                ? 0.0
                : food.getTotalCarbohydrates();
        double lipids = food.getTotalLipids() == null
                ? 0.0
                : food.getTotalLipids();

        return (int) (proteins * 4 + carbs * 4 + lipids * 9);
    }
}
