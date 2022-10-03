package br.com.business.sacola.resource;

import br.com.business.sacola.model.Item;
import br.com.business.sacola.model.Sacola;
import br.com.business.sacola.resource.dto.ItemDto;
import br.com.business.sacola.service.SacolaService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(value = "/ifood-devWeek/sacolas")
@RestController
@RequestMapping("/ifood-devWeek/sacolas")
@RequiredArgsConstructor
public class SacolaResource {
    private final SacolaService sacolaService;

    @PostMapping
    public Item incluirItemNaSacola(@RequestBody ItemDto itemDto) {
        return sacolaService.incluirItemNaSacola(itemDto);
    }

    @GetMapping("/{id}")
    public Sacola verSacola(@PathVariable ("id") Long id) {
        return sacolaService.verSacola(id);
    }

    @PatchMapping("/fecharsacola/{idSacola}")
    public Sacola fecharSacola(@PathVariable("idSacola") Long idSacola, @RequestParam("formaPagamento") int formaPagamento) {
        return sacolaService.fecharSacola(idSacola, formaPagamento);
    }

}
