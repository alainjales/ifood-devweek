package br.com.business.sacola.service;

import br.com.business.sacola.model.Item;
import br.com.business.sacola.model.Sacola;
import br.com.business.sacola.resource.dto.ItemDto;

public interface SacolaService {

    Sacola verSacola(Long id);
    Sacola fecharSacola(Long id, int formaPagamento);
    Item incluirItemNaSacola(ItemDto itemDto);
}
