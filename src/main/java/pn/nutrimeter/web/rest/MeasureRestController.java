package pn.nutrimeter.web.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pn.nutrimeter.service.models.MeasureServiceModel;
import pn.nutrimeter.service.services.api.MeasureService;
import pn.nutrimeter.web.models.binding.MeasureCreateBindingModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Validated
@RestController
public class MeasureRestController extends BaseRestController {

    private final MeasureService measureService;

    private final ModelMapper modelMapper;

    public MeasureRestController(MeasureService measureService, ModelMapper modelMapper) {
        this.measureService = measureService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/measure/add")
    public ResponseEntity<List<MeasureServiceModel>> addMeasures(
            @Valid @RequestBody List<MeasureCreateBindingModel> bindingModelList
    ) {
        ArrayList<MeasureServiceModel> measureServiceModels = new ArrayList<>();

        bindingModelList.forEach(m -> {
            MeasureServiceModel measureServiceModel = this.modelMapper.map(m, MeasureServiceModel.class);
            measureServiceModels.add(measureServiceModel);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(this.measureService.createAll(measureServiceModels));
    }

    @PostMapping("/measure/delete")
    public ResponseEntity deleteMeasure(@RequestBody Map<String, String> payload) {
        String id = payload.get("id");
        this.measureService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
