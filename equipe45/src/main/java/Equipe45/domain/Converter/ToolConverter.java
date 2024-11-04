/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Equipe45.domain.Converter;

import Equipe45.domain.DTO.ToolDTO;
import Equipe45.domain.Tool;

/**
 *
 * @author mat18
 */

public class ToolConverter {
    public ToolDTO convertToDTOFrom(Tool tool) {
        return new ToolDTO(tool.getName(), tool.getCutWidth(), tool.getPositionCharger());
    }

    public Tool convertToToolFrom(ToolDTO toolDTO) {
        return new Tool(toolDTO.name, toolDTO.cutWidth, toolDTO.positionCharger);
    }
}


