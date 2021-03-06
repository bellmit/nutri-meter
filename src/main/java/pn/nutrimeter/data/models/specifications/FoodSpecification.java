package pn.nutrimeter.data.models.specifications;

import pn.nutrimeter.data.models.Food;
import pn.nutrimeter.data.models.base.BaseSpecification;

public class FoodSpecification extends BaseSpecification<Food> {
    public FoodSpecification(SearchCriteria criteria) { super(criteria); }

    // USE SpecificationBuilder <E> join INSTEAD OF ALL THOSE METHODS
//    public static Specification<Food> joinCustomFoods(String username) {
//        return (Specification<Food>) (root, criteriaQuery, criteriaBuilder) -> {
//            Join<Food, User> userFood = root.join("user");
//            return criteriaBuilder.equal(userFood.get("username"), username);
//        };
//    }
//
//    public static Specification<Food> joinFavoriteFoods(String username) {
//        return (Specification<Food>) (root, criteriaQuery, criteriaBuilder) -> {
//            Join<Food, User> userFood = root.join("users");
//            return criteriaBuilder.equal(userFood.get("username"), username);
//        };
//    }
//
//    public static Specification<Food> joinCategories(String categoryName) {
//        return (Specification<Food>) (root, criteriaQuery, criteriaBuilder) -> {
//            Join<Food, FoodCategory> userFood = root.join("foodCategories");
//            return criteriaBuilder.equal(userFood.get("name"), categoryName);
//        };
//    }
}
