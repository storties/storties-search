package org.example.stortiessearch.infrastructure.search.query;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.data.search.post.document.PostDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final ESPostSearchQuery esPostSearchQuery;

    @GetMapping("/query-title")
    public List<PostDocument> queryByString(@RequestParam String input) {
        return esPostSearchQuery.searchByTitleAndTags(input);
    }

    @GetMapping("/vector")
    public List<PostDocument> queryByVector(@RequestParam String input) {
        return esPostSearchQuery.searchByKNN(input);
    }
}
