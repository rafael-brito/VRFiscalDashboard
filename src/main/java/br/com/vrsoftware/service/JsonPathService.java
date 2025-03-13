package br.com.vrsoftware.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JsonPathService {

    /**
     * Extract issue IDs from a JSON response using JSONPath
     */
    public List<String> extractIssueIds(String jsonResponse) {
        return JsonPath.read(jsonResponse, "$.issues[*].id");
    }
}
