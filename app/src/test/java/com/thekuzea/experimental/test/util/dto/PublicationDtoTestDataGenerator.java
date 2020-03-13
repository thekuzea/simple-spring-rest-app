package com.thekuzea.experimental.test.util.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.api.dto.PublicationDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicationDtoTestDataGenerator {

    public static PublicationDto createPublicationDto() {
        return PublicationDto.builder()
                .id("95377ef8-3010-45a9-a5f7-296b98f314b6")
                .publicationTime("2020-04-26T11:48:54+02:00")
                .publishedBy("Larry")
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();
    }

    public static PublicationDto createPublicationDtoAsNewPublication() {
        return PublicationDto.builder()
                .publicationTime("2020-02-26T14:21:43+02:00")
                .topic("Achillea alpina L.")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }

    public static List<PublicationDto> createPublicationDtoList() {
        final String otherUser = "Kate";
        final PublicationDto publicationDto1 = PublicationDto.builder()
                .id("95377ef8-3010-45a9-a5f7-296b98f314b6")
                .publishedBy(UserDtoTestDataGenerator.createUserDto().getUsername())
                .publicationTime("2020-04-26T11:48:54+02:00")
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();

        final PublicationDto publicationDto2 = PublicationDto.builder()
                .id("feea949a-f2f3-44ec-b861-0bb521fd7b54")
                .publishedBy(otherUser)
                .publicationTime("2020-03-14T17:41:34+02:00")
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final PublicationDto publicationDto3 = PublicationDto.builder()
                .id("b339b3f6-1826-4b62-9f37-742339310075")
                .publishedBy(otherUser)
                .publicationTime("2020-01-29T06:27:59+02:00")
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publicationDto1, publicationDto2, publicationDto3);
    }

    public static List<PublicationDto> createPublicationDtoListForCurrentUser() {
        final String publishedBy = "Kate";
        final PublicationDto publicationDto1 = PublicationDto.builder()
                .id("feea949a-f2f3-44ec-b861-0bb521fd7b54")
                .publishedBy(publishedBy)
                .publicationTime("2020-03-14T17:41:34+02:00")
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final PublicationDto publicationDto2 = PublicationDto.builder()
                .id("b339b3f6-1826-4b62-9f37-742339310075")
                .publishedBy(publishedBy)
                .publicationTime("2020-01-29T06:27:59+02:00")
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publicationDto1, publicationDto2);
    }

    public static PublicationDto createPublicationDtoAsNewPublicationWrongPublisher() {
        return PublicationDto.builder()
                .publicationTime("2020-04-13T03:41:16+02:00")
                .topic("Achillea alpina L.")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }

    public static PublicationDto createPublicationDtoAsNewPublicationWhereItAlreadyExists() {
        return PublicationDto.builder()
                .publicationTime("2020-04-13T03:41:16+02:00")
                .topic("Carex adusta Boott")
                .body("In hac habitasse platea dictumst. Aliquam augue quam, sollicitudin vitae, consectetuer eget, rutrum at, lorem. Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.")
                .build();
    }
}
