package cl.bibliotecaam.resenia.msresenia.assembler;
import cl.bibliotecaam.resenia.msresenia.controller.ReseniaController;
import cl.bibliotecaam.resenia.msresenia.dto.ReseniaResponseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReseniaModelAssembler implements RepresentationModelAssembler<ReseniaResponseDTO, EntityModel<ReseniaResponseDTO>> {

    @Override
    public EntityModel<ReseniaResponseDTO> toModel(ReseniaResponseDTO reseniaDto){
        return EntityModel.of(reseniaDto,
                linkTo(methodOn(ReseniaController.class).obtenerPorId(reseniaDto.getId_resenia())).withSelfRel(),
                linkTo(methodOn(ReseniaController.class).obtenerTodas()).withRel("resenias"));
    }
}
