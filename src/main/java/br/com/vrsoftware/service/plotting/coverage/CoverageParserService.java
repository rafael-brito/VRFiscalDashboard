package br.com.vrsoftware.service.plotting.coverage;

import br.com.vrsoftware.dto.plotting.CoverageDataDTO;
import br.com.vrsoftware.exceptions.ParsingException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CoverageParserService {

    private static final String DATASET_CSV = "src/main/resources/datasets/Coverage_2025.csv";

    // Functional interface for line splitting strategy
    @FunctionalInterface
    private interface CoverageLineSplitter {
        CoverageDataDTO parse(String line);
    }

    // Default line splitter that uses comma
    private static final CoverageLineSplitter DEFAULT_COVERAGE_PARSER = line -> {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid line format: " + line);
        }

        String month = parts[0].trim();
        double estimated = Double.parseDouble(parts[1].trim());
        double actual = Double.parseDouble(parts[2].trim());

        return new CoverageDataDTO(month, estimated, actual);
    };

    // Function that processes input lines from a file and converts to CoverageDataDTO list
    public static List<CoverageDataDTO> parseCoverageCsv() {
        Path path = Paths.get(DATASET_CSV);
        try (Stream<String> lines = Files.lines(path)) {
                return lines
                        .skip(1) // Skip header line
                        .filter(line -> !line.trim().isEmpty()) // Skip empty lines
                        .map(DEFAULT_COVERAGE_PARSER::parse)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ParsingException("Error reading file: " + DATASET_CSV, e);
        }
    }

    // Filter coverage data based on a predicate
    public static List<CoverageDataDTO> filterCoverageData(
            List<CoverageDataDTO> data,
            Predicate<CoverageDataDTO> predicate) {
        return data.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // Transform coverage data using a mapping function
    public static <R> List<R> transformCoverageData(
            List<CoverageDataDTO> data,
            Function<CoverageDataDTO, R> mapper) {
        return data.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
