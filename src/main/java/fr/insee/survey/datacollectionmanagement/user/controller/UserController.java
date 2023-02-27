package fr.insee.survey.datacollectionmanagement.user.controller;

import fr.insee.survey.datacollectionmanagement.constants.Constants;
import fr.insee.survey.datacollectionmanagement.contact.controller.ContactController;
import fr.insee.survey.datacollectionmanagement.contact.domain.Contact;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactDto;
import fr.insee.survey.datacollectionmanagement.contact.dto.ContactFirstLoginDto;
import fr.insee.survey.datacollectionmanagement.metadata.domain.Source;
import fr.insee.survey.datacollectionmanagement.metadata.service.SourceService;
import fr.insee.survey.datacollectionmanagement.questioning.domain.Questioning;
import fr.insee.survey.datacollectionmanagement.questioning.domain.QuestioningAccreditation;
import fr.insee.survey.datacollectionmanagement.user.domain.SourceAccreditation;
import fr.insee.survey.datacollectionmanagement.user.domain.User;
import fr.insee.survey.datacollectionmanagement.user.dto.UserDto;
import fr.insee.survey.datacollectionmanagement.user.service.SourceAccreditationService;
import fr.insee.survey.datacollectionmanagement.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
        + "|| @AuthorizeMethodDecider.isWebClient() ")
@Tag(name = "7-User", description = "Enpoints to create, update, delete and find users, their events and accreditations")
@Slf4j
public class UserController {

    static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserService userService;

    @Autowired
    SourceService sourceService;

    @Autowired
    SourceAccreditationService sourceAccreditationService;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Search for users, paginated")
    @GetMapping(value = Constants.API_USERS_ALL, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserController.UserPage.class)))
    })
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "identifier") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<User> pageC = userService.findAll(pageable);
        List<UserDto> listC = pageC.stream().map(c -> convertToDto(c)).collect(Collectors.toList());
        return ResponseEntity.ok().body(new UserController.UserPage(listC, pageable, pageC.getTotalElements()));
    }

    @Operation(summary = "Search for a user by its id")
    @GetMapping(value = Constants.API_USERS_ID, produces = "application/json")
    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<?> getUser(@PathVariable("id") String id) {
        Optional<User> user = userService.findByIdentifier(StringUtils.upperCase(id));
        try {
            if (user.isPresent())
                return ResponseEntity.ok().body(convertToDto(user.get()));
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user does not exist");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

    }

    @Operation(summary = "Update or create user")
    @PutMapping(value = Constants.API_USERS_ID, produces = "application/json", consumes = "application/json")
    @PreAuthorize("@AuthorizeMethodDecider.isInternalUser() "
            + "|| @AuthorizeMethodDecider.isWebClient() "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<?> putUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        if (StringUtils.isBlank(userDto.getIdentifier()) || !userDto.getIdentifier().equalsIgnoreCase(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id and user identifier don't match");
        }
        User user;
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(userDto.getIdentifier()).toUriString());

        try {
            user = convertToEntity(userDto);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible to parse user");
        } catch (NoSuchElementException e) {
            LOGGER.info("Creating user with the identifier {}", userDto.getIdentifier());
            user = convertToEntityNewContact(userDto);

            User userCreate = userService.createUserEvent(user, null);
            return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(convertToDto(userCreate));

        }

        User userUpdate = userService.updateUserEvent(user, null);
        return ResponseEntity.ok().headers(responseHeaders).body(convertToDto(userUpdate));
    }


    @Operation(summary = "Delete a user, its userEvents and its sourceaccreditations")
    @DeleteMapping(value = Constants.API_USERS_ID)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        try {
            Optional<User> user = userService.findByIdentifier(id);
            if (user.isPresent()) {
                userService.deleteContactAddressEvent(user.get());

                sourceAccreditationService.findByUserIdentifier(id).stream().forEach(acc -> {
                    Source source = sourceService.findById(acc.getSource().getId()).get();
                    Set<SourceAccreditation> newSet = source.getSourceAccreditations();
                    newSet.removeIf(a -> a.getId().equals(acc.getId()));
                    source.setSourceAccreditations(newSet);
                    sourceService.insertOrUpdateSource(source);
                    sourceAccreditationService.deleteAccreditation(acc);

                });
                log.info("Delete user {}", id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }
    }




    private User convertToEntity(UserDto userDto) throws ParseException, NoSuchElementException {
        User user = modelMapper.map(userDto, User.class);

        Optional<User> oldUser = userService.findByIdentifier(userDto.getIdentifier());
        if (!oldUser.isPresent()) {
            throw new NoSuchElementException();
        }
        user.setRole(oldUser.get().getRole());
        user.setUserEvents(oldUser.get().getUserEvents());

        return user;
    }

    private User convertToEntityNewContact(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
    class UserPage extends PageImpl<UserDto> {

        private static final long serialVersionUID = 656181199902518234L;

        public UserPage(List<UserDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }
    }
}
