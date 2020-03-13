package com.thekuzea.experimental.test.util.model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.thekuzea.experimental.domain.model.Publication;
import com.thekuzea.experimental.support.util.DateTimeUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicationModelTestDataGenerator {

    public static Publication createPublication() {
        return Publication.builder()
                .id(UUID.fromString("95377ef8-3010-45a9-a5f7-296b98f314b6"))
                .publishedBy(UserModelTestDataGenerator.createUser())
                .publicationTime(DateTimeUtils.convertStringToOffsetDateTime("2020-04-26T11:48:54+02:00"))
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();
    }

    public static List<Publication> createPublicationList() {
        final Publication publication1 = Publication.builder()
                .id(UUID.fromString("95377ef8-3010-45a9-a5f7-296b98f314b6"))
                .publishedBy(UserModelTestDataGenerator.createUser())
                .publicationTime(DateTimeUtils.convertStringToOffsetDateTime("2020-04-26T11:48:54+02:00"))
                .topic("Hieracium venosum L.")
                .body("Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh.")
                .build();

        final Publication publication2 = Publication.builder()
                .id(UUID.fromString("feea949a-f2f3-44ec-b861-0bb521fd7b54"))
                .publishedBy(UserModelTestDataGenerator.createUser())
                .publicationTime(DateTimeUtils.convertStringToOffsetDateTime("2020-03-14T17:41:34+02:00"))
                .topic("Carex adusta Boott")
                .body("Integer aliquet, massa id lobortis convallis, tortor risus dapibus augue, vel accumsan tellus nisi eu orci. Mauris lacinia sapien quis libero. Nullam sit amet turpis elementum ligula vehicula consequat. Morbi a ipsum. Integer a nibh. In quis justo. Maecenas rhoncus aliquam lacus.")
                .build();

        final Publication publication3 = Publication.builder()
                .id(UUID.fromString("b339b3f6-1826-4b62-9f37-742339310075"))
                .publishedBy(UserModelTestDataGenerator.createUser())
                .publicationTime(DateTimeUtils.convertStringToOffsetDateTime("2020-01-29T06:27:59+02:00"))
                .topic("Collinsia heterophylla Buist ex Graham")
                .body("Maecenas ut massa quis augue luctus tincidunt. Nulla mollis molestie lorem. Quisque ut erat. Curabitur gravida nisi at nibh. In hac habitasse platea dictumst.")
                .build();

        return Arrays.asList(publication1, publication2, publication3);
    }
}
