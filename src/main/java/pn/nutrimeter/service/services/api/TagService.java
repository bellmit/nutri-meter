package pn.nutrimeter.service.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import pn.nutrimeter.service.models.TagServiceModel;

import java.util.List;

public interface TagService {

    void create(TagServiceModel tagServiceModel);

    List<TagServiceModel> getAll();

    TagServiceModel getById(String tagId);

    void deleteTag(String tagId);

    void edit(TagServiceModel tagServiceModel);
}
