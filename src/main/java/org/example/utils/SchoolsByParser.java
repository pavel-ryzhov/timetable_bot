package org.example.utils;

import kotlin.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.entities.Lesson;
import org.example.entities.SchoolDay;
import org.example.entities.Time;
import org.example.entities.Timetable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SchoolsByParser {

//    public static void main(String[] args) throws IOException {
//        new SchoolsByParser("Ryzhov Pavel", "21092006").getTimetable();
//    }

    private static final String BASE_URL = "https://schools.by/login";

    private static final Logger LOGGER = LogManager.getLogger(SchoolsByParser.class);

    private final String username;
    private final String password;
    private final RequestsService requestsService = RequestsService.getInstance();

    public SchoolsByParser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timetable getTimetable() throws IOException {
        var token = parseToken(requestsService.executeGetRequest(BASE_URL).body().string());
        var loginResponse = requestsService.executePostRequest(BASE_URL, Map.of("referer", BASE_URL), Map.of(
                "csrfmiddlewaretoken", token,
                "username", username,
                "password", password,
                "|123", "|123"
        ));
        var loginResponseString = loginResponse.body().string();
        var newBaseUrl = formatBaseUrl(loginResponse.request().url().toString());
        var timetableUrl = newBaseUrl + parseTimetableUrl(loginResponseString);
        var recordBookUrl = newBaseUrl + parseRecordBookUrl(loginResponseString);
        var quarterUrl = newBaseUrl + parseQuarterUrl(requestsService.executeGetRequest(recordBookUrl).body().string());
        var lessonsTimes = formatLessonsTimes(parseLessonsTimes(requestsService.executeGetRequest(timetableUrl).body().string()));
        return formatTimetable(parseTimetable(requestsService.executeGetRequest(quarterUrl).body().string()), lessonsTimes);
    }

    private static Timetable formatTimetable(List<List<String>> stringTimetable, List<Pair<Time, Time>> lessonsTimes) {
        var days = new ArrayList<SchoolDay>(5);
        for (var stringDay : stringTimetable) {
            var listDay = new ArrayList<Lesson>();
            for (int i = 0; i < stringDay.size(); i++)
                listDay.add(new Lesson(lessonsTimes.get(i).getFirst(), lessonsTimes.get(i).getSecond(), Lesson.Subject.getSubjectByName(stringDay.get(i))));
            days.add(new SchoolDay(listDay));
        }
        return new Timetable(days);
    }

    private static List<List<String>> parseTimetable(String str) {
        var result = new ArrayList<List<String>>(5);
        var pattern = "<tbody>";
        var beginIndex = str.indexOf(pattern);
        while (beginIndex != -1) {
            var dayResult = new ArrayList<String>(7);
            var tbody = parse(str, pattern, "</tbody>", beginIndex);
            var patternInTBody = "<td class=\"lesson \">\n<span>";
            var beginIndexInTBody = tbody.indexOf(patternInTBody);
            while (beginIndexInTBody != -1) {
                var lesson = parse(tbody, patternInTBody, "</span>", beginIndexInTBody).replaceFirst("\\d\\.", "").replace("\n", "").trim();
                if (!lesson.isBlank())
                    dayResult.add(lesson);
                beginIndexInTBody = tbody.indexOf(patternInTBody, beginIndexInTBody + 1);
            }
            if (!dayResult.isEmpty())
                result.add(dayResult);
            beginIndex = str.indexOf(pattern, beginIndex + 1);
        }
        return result;
    }

    private static String formatBaseUrl(String str) {
        return str.substring(0, str.indexOf(".schools.by") + 11);
    }

    private static String parseToken(String str) {
        var token = parse(str, "name=\"csrfmiddlewaretoken\" value=\"");
        LOGGER.info("Token parsed: " + token);
        return token;
    }

    private static String parseQuarterUrl(String str) {
        var result = parse(str, "<a class=\"current\" src=\"");
        LOGGER.info("Quarter url parsed: " + result);
        return result;
    }

    private static String parseTimetableUrl(String str) {
        var result = parse(str, "href=\"#timetable\" src=\"");
        LOGGER.info("Timetable url parsed: " + result);
        return result;
    }

    private static String parseRecordBookUrl(String str) {
        var result = parse(str, "href=\"#dnevnik\" src=\"");
        LOGGER.info("Record book url parsed: " + result);
        return result;
    }

    private static List<String> parseLessonsTimes(String str) {
        var div = parse(str, "<div class=\"ttb_box\">", "</div>\n</div>");
        var tbody = parse(div, "<tbody>", "</tbody>");
        var pattern = "<td class=\"time\">";
        var beginIndex = tbody.indexOf(pattern);
        var result = new ArrayList<String>(7);
        while (beginIndex != -1) {
            result.add(parse(tbody, pattern, "</td>", beginIndex).trim());
            beginIndex = tbody.indexOf(pattern, beginIndex + 1);
        }
        return result;
    }

    private static List<Pair<Time, Time>> formatLessonsTimes(List<String> stringTimes) {
        var result = new ArrayList<Pair<Time, Time>>();
        for (var stringTime : stringTimes) {
            var times = stringTime.split("\n&ndash;\n");
            var startTime = times[0].split(":");
            var endTime = times[1].split(":");
            result.add(new Pair<>(
                    new Time(Integer.parseInt(startTime[0]), Integer.parseInt(startTime[1])),
                    new Time(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]))
            ));
        }
        LOGGER.info("Lessons times parsed: " + result);
        return result;
    }

    private static String parse(String str, String pattern) {
        return parse(str, pattern, "\"");
    }

    private static String parse(String str, String pattern, String endPattern) {
        return parse(str, pattern, endPattern, 0);
    }

    private static String parse(String str, String pattern, String endPattern, int beginPatternIndex) {
        var beginIndex = str.indexOf(pattern, beginPatternIndex) + pattern.length();
        var endIndex = str.indexOf(endPattern, beginIndex + 1);
        return str.substring(beginIndex, endIndex);
    }


    private static void writeToFile(String str, String filePath) {
        try {
            Files.writeString(Path.of(filePath), str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
