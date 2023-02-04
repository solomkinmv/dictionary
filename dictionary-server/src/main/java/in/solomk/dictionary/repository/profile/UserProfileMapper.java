package in.solomk.dictionary.repository.profile;

import in.solomk.dictionary.repository.profile.document.UserProfileDocument;
import in.solomk.dictionary.service.profile.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileDocument toDocument(UserProfile userProfile);

    @Mapping(target = "withLanguages", ignore = true)
    UserProfile toModel(UserProfileDocument userProfileDocument);
}
