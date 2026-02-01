package com.mocktest.mocktest.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuestionDTO {
    private Long questionId;
    private String questionTextEn;
    private String questionTextTe;
    private List<OptionDTO> options;
}