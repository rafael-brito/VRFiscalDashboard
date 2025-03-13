package br.com.vrsoftware.util;

import br.com.vrsoftware.dto.jira.WorklogDTO;

import java.util.List;

/**
 * Helper class to extract text from the nested comment structure on IssueDTO
 */
public class CommentTextExtractor {

    /**
     * Extract plain text from a potentially nested comment object
     *
     * @param comment The comment object from Jira API
     * @return Plain text content of the comment
     */
    public static String extractText(WorklogDTO.Comment comment) {
        if (comment == null) {
            return "";
        }

        // Process the comment's content list
        return extractTextFromContentList(comment.getContent());
    }

    /**
     * Recursively process a list of content objects
     */
    private static String extractTextFromContentList(List<WorklogDTO.Content> contentList) {
        if (contentList == null || contentList.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (WorklogDTO.Content content : contentList) {
            // Add direct text if available
            if (content.getText() != null && !content.getText().isEmpty()) {
                sb.append(content.getText());
            }

            // Process nested content recursively
            if (content.getContent() != null && !content.getContent().isEmpty()) {
                sb.append(extractTextFromContentList(content.getContent()));
            }
        }

        return sb.toString();
    }
}
