package br.com.vrsoftware.service.jira;

import br.com.vrsoftware.dto.CustomValuesDTO;
import br.com.vrsoftware.dto.jira.WorklogDTO;
import br.com.vrsoftware.dto.jira.issue.IssueDTO;
import br.com.vrsoftware.util.CommentTextExtractor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


@Service
public class SummarizeService {

    private static final double HOURLY_RATE = 250.0;

    /**
     * Atualiza os textos dos comentários extraídos dos worklogs de uma issue.
     *
     * Para cada worklog da issue, extrai o texto do comentário utilizando o {@link CommentTextExtractor}
     * e armazena o resultado no objeto {@link CustomValuesDTO} do worklog.
     *
     * @param issue A issue contendo os worklogs que terão seus comentários processados
     */
    public void updateWorklogExtractedText(IssueDTO issue) {
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            worklog.getCustomValues().setWorklogCommentText(CommentTextExtractor.extractText(worklog.getComment()));
        }
    }

    /**
     * Atualiza a data de início formatada dos worklogs de uma issue.
     *
     * Para cada worklog da issue, converte a data de início do formato ISO 8601 para o formato
     * "dd MMM yyyy" utilizando a localidade "pt-BR" e armazena o resultado no objeto {@link CustomValuesDTO} do worklog.
     *
     * @param issue A issue contendo os worklogs que terão suas datas de início formatadas
     */
    public void updateWorklogStartedTime(IssueDTO issue) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", new Locale("pt", "BR"));
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            OffsetDateTime dateTime = OffsetDateTime.parse(worklog.getStarted(), inputFormatter);
            worklog.getCustomValues().setWorklogFormattedStartDate(dateTime.format(outputFormatter));
        }
    }

    /**
     * Atualiza o total de horas trabalhadas em uma issue.
     *
     * Para cada worklog da issue, calcula o tempo gasto em horas e armazena no objeto {@link CustomValuesDTO} do worklog.
     * Em seguida, calcula o total de horas trabalhadas somando o tempo gasto de todos os worklogs e armazena no objeto {@link CustomValuesDTO} da issue.
     *
     * @param issue A issue contendo os worklogs que terão seus tempos de trabalho processados
     */
    public void updateIssueTotalHours(IssueDTO issue) {
        for (WorklogDTO worklog : issue.getFields().getWorklog().getWorklogs()) {
            worklog.getCustomValues().setWorklogTimeSpentHours(worklog.getTimeSpentSeconds() / 3600.0);
        }
        double totalHours = issue.getFields().getWorklog().getWorklogs().stream()
                .mapToDouble(x -> x.getCustomValues().getWorklogTimeSpentHours()).sum();
        issue.getCustomValues().setIssueTotalHours(totalHours);
    }

    /**
     * Atualiza o custo de desenvolvimento de uma issue.
     *
     * Define a taxa horária da issue como um valor constante e calcula o custo total
     * de desenvolvimento multiplicando o total de horas trabalhadas pela taxa horária.
     * O resultado é armazenado no objeto {@link CustomValuesDTO} da issue.
     *
     * @param issue A issue que terá seu custo de desenvolvimento atualizado
     */
    public void updateIssueDevelopmentCost(IssueDTO issue) {
        issue.getCustomValues().setIssueHourlyRate(HOURLY_RATE);
        double totalCost = issue.getCustomValues().getIssueTotalHours() * issue.getCustomValues().getIssueHourlyRate();
        issue.getCustomValues().setIssueDevelopmentCost(totalCost);
    }
}
