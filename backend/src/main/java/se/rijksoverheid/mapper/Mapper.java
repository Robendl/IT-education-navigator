package se.rijksoverheid.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

/**
 * The Mapper class is mainly used for converting Data Transfer Objects to and from entity objects.
 * This class is created so that the configurations don't have to be set each time a ModelMapper is required.
 */
public abstract class Mapper {
    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Calls the map function on a ModelMapper object.
     * @param source            source object
     * @param destinationType   destination type
     * @return                  mapped object of destination type
     * @param <S>               Source type
     * @param <D>               destination class
     */
    public static <S, D> D map(S source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    /**
     * Calls the map function on a ModelMapper object.
     * @param source            source object
     * @param destination       destination object
     * @param <S>               source class
     * @param <D>               destination class
     */
    public static <S, D> void map(S source, D destination) {
        modelMapper.map(source, destination);
    }
}
