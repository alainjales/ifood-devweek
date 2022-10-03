package br.com.business.sacola.service.implementation;

import br.com.business.sacola.enumeration.FormaPagamento;
import br.com.business.sacola.model.Item;
import br.com.business.sacola.model.Produto;
import br.com.business.sacola.model.Restaurante;
import br.com.business.sacola.model.Sacola;
import br.com.business.sacola.repository.ItemRepository;
import br.com.business.sacola.repository.ProdutoRepository;
import br.com.business.sacola.repository.SacolaRepository;
import br.com.business.sacola.resource.dto.ItemDto;
import br.com.business.sacola.service.SacolaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SacolaServiceImplementation implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;

    private final ItemRepository itemRepository;
    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola não existe");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int formaPagamento) {
        Sacola sacola = verSacola(id);
        if(sacola.getItens().isEmpty()){
            throw new RuntimeException("Sacola vazia.");
        }

        FormaPagamento numeroFormaPagamento = (formaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.CARTAO);
        sacola.setFormaPagamento(numeroFormaPagamento);
        sacola.setFechada(true);

        return sacolaRepository.save(sacola);
    }

    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {

        Sacola sacola = verSacola(itemDto.getSacolaId());

        Produto produto = produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                () -> {
                    throw new RuntimeException("Produto não encontrado.");
                }
        );

        if(sacola.getFechada()) {
            throw new RuntimeException("Sacola está fechada.");
        }

        Item itemPraInserir = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produto)
                .build();

        List<Item> itensDaSacola = sacola.getItens();
        if(itensDaSacola.isEmpty()){
            itensDaSacola.add(itemPraInserir);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            if(itemPraInserir.getProduto().getRestaurante() == restauranteAtual) {
                itensDaSacola.add(itemPraInserir);
            } else {
                throw new RuntimeException("Não pode inserir produtos de restaurantes diferentes na mesma sacola!");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();
        for (Item item : itensDaSacola) {
            valorDosItens.add(item.getQuantidade() * item.getProduto().getValorUnitario());
        }

        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return itemPraInserir;
    }
}
