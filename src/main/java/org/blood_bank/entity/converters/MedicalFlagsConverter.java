package org.blood_bank.entity.converters;

import org.blood_bank.entity.enums.MedicalFlags;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicalFlagsConverter implements AttributeConverter<Set<MedicalFlags>, String> {

    private static final String DELIMITER = ", ";
    @Override
    public String convertToDatabaseColumn(Set<MedicalFlags> flags) {
        if(flags == null || flags.isEmpty()) {
            return null;
        }

        return flags.stream().map(Enum::name)
                .collect(Collectors.joining(DELIMITER));

    }

    @Override
    public Set<MedicalFlags> convertToEntityAttribute(String dbData) {

        if(dbData == null || dbData.trim().isEmpty()) {
            return new HashSet<>();
        }

        return Arrays.stream(dbData.split(DELIMITER)).
                map(String::trim).map(MedicalFlags::valueOf)
                .collect(Collectors.toSet());
    }
}
