package com.mocktest.mocktest.controllers;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mocktest.mocktest.dto.ExamResultDTO;
import com.mocktest.mocktest.dto.OptionDTO;
import com.mocktest.mocktest.dto.QuestionDTO;

import com.mocktest.mocktest.entities.Option;
import com.mocktest.mocktest.entities.Question;
import com.mocktest.mocktest.entities.User;
import com.mocktest.mocktest.repository.QuestionRepository;
import com.mocktest.mocktest.services.UserService;

@Controller
public class ExamController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionRepository questionRepository;

    /* ================== EXAM LOGIN ================== */
    @PostMapping("/exam_login")
    public String startExam(
            @RequestParam("examType") String examType,
            @RequestParam("setNo") int setNo,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        session.setAttribute("examType", examType);
        session.setAttribute("setNo", setNo);

        model.addAttribute("systemName", "C001");
        model.addAttribute("candidateName", user.getName());
        model.addAttribute("subject", examType + " - Set " + setNo);

        return "exam/exam_login";
    }

    /* ================== INSTRUCTIONS ================== */
    @GetMapping("/instructions")
    public String instructions1(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
    model.addAttribute("candidateName", user.getName());
        return "exam/instructions";
    }

    @GetMapping("/instructions2")
    public String instructions2(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("candidateName", user.getName());
        return "exam/instructions2";
    }

    /* ================== EXAM BEGIN ================== */
  @GetMapping("/exambegin")
public String exambegin(HttpSession session, Model model) {

    String examType = (String) session.getAttribute("examType");
    Integer setNo = (Integer) session.getAttribute("setNo");

    // SAFETY CHECK
    if (examType == null || setNo == null) {
        return "redirect:/instructions";
    }

    // Fetch questions
    List<Question> questions =
            questionRepository.findByExam_ExamNameAndExam_SetNo(examType, setNo);

    if (questions == null || questions.isEmpty()) {
        model.addAttribute("error", "No questions found for this exam");
        return "exam/error";
    }

    // ENTITY → DTO
    List<QuestionDTO> questionDTOs = new ArrayList<>();

    for (Question q : questions) {

        QuestionDTO qdto = new QuestionDTO();
        qdto.setQuestionId(q.getQuestionId());
        qdto.setQuestionTextEn(q.getQuestionTextEn());
        qdto.setQuestionTextTe(q.getQuestionTextTe());

        List<OptionDTO> optionDTOs = new ArrayList<>();

        for (Option o : q.getOptions()) {
            OptionDTO odto = new OptionDTO();
            odto.setOptionId(o.getOptionId());
            odto.setOptionTextEn(o.getOptionTextEn());
            odto.setOptionTextTe(o.getOptionTextTe());
            optionDTOs.add(odto);
        }

        qdto.setOptions(optionDTOs);
        questionDTOs.add(qdto);
    }

    // Store exam start time (for timer & result)
    session.setAttribute("examStartTime", System.currentTimeMillis());

    // Send to UI
    model.addAttribute("questions", questionDTOs);
    model.addAttribute("totalQuestions", questionDTOs.size());
    model.addAttribute("examType", examType);

    return "exam/exambegin";
}
    @GetMapping("exam/error")
    public String examerror() {
        return "exam/error";
    }
    

    /* ================== SUBMIT EXAM ================== */
    @PostMapping("/exam/submit")
    @ResponseBody
    public ExamResultDTO submitExam(
            @RequestBody Map<Long, Long> answers,
            HttpSession session) {

        String examType = (String) session.getAttribute("examType");
        Integer setNo = (Integer) session.getAttribute("setNo");

        Long startTime = (Long) session.getAttribute("examStartTime");
        long timeTakenMinutes =
                (System.currentTimeMillis() - startTime) / (1000 * 60);

        List<Question> questions =
                questionRepository.findByExam_ExamNameAndExam_SetNo(examType, setNo);

        int score = 0;
        int totalMarks = 0;

        for (Question q : questions) {
            totalMarks += q.getMarks();

            Long selectedOptionId = answers.get(q.getQuestionId());
            if (selectedOptionId != null) {
                for (Option o : q.getOptions()) {
                    if (o.getOptionId().equals(selectedOptionId) && o.isCorrect()) {
                        score += q.getMarks();
                    }
                }
            }
        }

        // Save result details in session
        session.setAttribute("score", score);
        session.setAttribute("totalMarks", totalMarks);
        session.setAttribute("totalQuestions", questions.size());
        session.setAttribute("timeTaken", timeTakenMinutes);

        return new ExamResultDTO(score, questions.size());
    }

    /* ================== RESULT PAGE ================== */
    @GetMapping("/exam/result")
    public String showResult(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        model.addAttribute("candidateName", user.getName());
        model.addAttribute("examType", session.getAttribute("examType"));
        model.addAttribute("setNo", session.getAttribute("setNo"));

        model.addAttribute("score", session.getAttribute("score"));
        model.addAttribute("totalMarks", session.getAttribute("totalMarks"));
        model.addAttribute("totalQuestions", session.getAttribute("totalQuestions"));
        model.addAttribute("timeTaken", session.getAttribute("timeTaken"));
        model.addAttribute("totalTime", 150); // exam duration in minutes

        return "exam/result";
    }
}
