package com.ggutierrez.pizzeria.service;

import com.ggutierrez.pizzeria.persistence.entity.PizzaEntity;
import com.ggutierrez.pizzeria.persistence.repository.PizzaPagSortRepository;
import com.ggutierrez.pizzeria.persistence.repository.PizzaRepository;
import com.ggutierrez.pizzeria.service.dto.UpdatePizzaPriceDto;
import com.ggutierrez.pizzeria.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {

    /* usandp JdbcTemplate

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PizzaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PizzaEntity> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM pizza WHERE available = 0", new BeanPropertyRowMapper<>(PizzaEntity.class));
    }

    */

    // usando repository

    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository) {
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    public List<PizzaEntity> getAll() {
        return this.pizzaRepository.findAll();
    }

    public Page<PizzaEntity> getAllPageable(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    public PizzaEntity get(int idPizza) {
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public List<PizzaEntity> getAllAvailable() {
        System.out.println(this.pizzaRepository.countByVeganTrue());
        return this.pizzaRepository.findAllByAvailableTrueOrderByPrice();
    }

    public Page<PizzaEntity> getAllAvailableSorted(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.pizzaPagSortRepository.findAllByAvailableTrue(pageable);
    }

    public PizzaEntity getByName(String name) {
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(() -> new RuntimeException("La pizza no existe"));
    }

    public List<PizzaEntity> getWith(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWithout(String ingredient) {
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getCheapest(double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity save(PizzaEntity pizza) {
        return this.pizzaRepository.save(pizza);
    }

//    @Transactional
//    public void updatePizzaPrice(int idPizza, double newPrice) {
//        this.pizzaRepository.updatePizzaPrice(idPizza, newPrice);
//    }

    @Transactional(noRollbackFor = EmailApiException.class)
    public void updatePizzaPrice(UpdatePizzaPriceDto pizzaPriceDto) {
        this.pizzaRepository.updatePizzaPrice(pizzaPriceDto);
        this.sendEmail();
    }

    private void sendEmail() {
        throw new EmailApiException();
    }

    public void delete(int id) {
        this.pizzaRepository.deleteById(id);
    }

    public boolean exist(int id) {
        return this.pizzaRepository.existsById(id);
    }
}
