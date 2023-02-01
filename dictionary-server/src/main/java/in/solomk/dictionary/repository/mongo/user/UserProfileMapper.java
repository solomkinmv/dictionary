package in.solomk.dictionary.repository.mongo.user;

import in.solomk.dictionary.repository.mongo.user.document.UserProfileDocument;
import in.solomk.dictionary.service.user.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileDocument toDocument(UserProfile userProfile);

    @Mapping(target = "withLanguages", ignore = true)
    UserProfile toModel(UserProfileDocument userProfileDocument);
}
