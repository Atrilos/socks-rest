package com.atrilos.socksrest.service;

import com.atrilos.socksrest.exception.NegativeSocksAmountException;
import com.atrilos.socksrest.exception.NoParameterProvidedException;
import com.atrilos.socksrest.model.ChangeSocksDTO;
import com.atrilos.socksrest.model.FilterSocks;
import com.atrilos.socksrest.model.Socks;
import com.atrilos.socksrest.model.Socks_;
import com.atrilos.socksrest.model.enums.Operation;
import com.atrilos.socksrest.repository.SocksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.allOf;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocksService {

    private final SocksRepository socksRepository;

    /**
     * Method for adding/removing socks
     *
     * @param changeSocksDTO dto-class that contains info about socks (positive quantity - addition, negative -removal)
     * @return saved Socks entity
     */
    @Transactional
    public Socks changeSocks(ChangeSocksDTO changeSocksDTO) {
        log.info("Change socks with {}", changeSocksDTO);
        Socks entity = socksRepository
                .findByColorAndCottonPart(changeSocksDTO.getColor(), changeSocksDTO.getCottonPart())
                .orElse(createNewEntity(changeSocksDTO));
        if (entity.getQuantity() + changeSocksDTO.getQuantity() < 0) {
            throw new NegativeSocksAmountException("Socks amount can't be negative!");
        }
        entity.setQuantity(entity.getQuantity() + changeSocksDTO.getQuantity());
        return socksRepository.save(entity);
    }

    private Socks createNewEntity(ChangeSocksDTO changeSocksDTO) {
        return Socks.builder()
                .color(changeSocksDTO.getColor())
                .cottonPart(changeSocksDTO.getCottonPart())
                .quantity(0L)
                .build();
    }

    /**
     * Method for retrieving number of socks after filtering
     *
     * @param filterSocks filter parameters
     * @return number of socks mathing given predicates
     */
    public Long getSocksFiltered(FilterSocks filterSocks) {
        log.info("Get socks with filter={}", filterSocks);
        List<Specification<Socks>> specificationList = createSpecificationList(filterSocks);
        if (specificationList.isEmpty()) {
            throw new NoParameterProvidedException("At least one parameter must be provided!");
        }
        List<Socks> foundByCriteria = socksRepository.findAll(allOf(specificationList));

        return foundByCriteria.stream().mapToLong(Socks::getQuantity).sum();
    }

    /**
     * Method returns specification list from filter-dto
     *
     * @param filterSocks filter parameters
     */
    private List<Specification<Socks>> createSpecificationList(FilterSocks filterSocks) {
        List<Specification<Socks>> list = new ArrayList<>();
        if (filterSocks.getColor() != null) {
            Specification<Socks> colorSpec =
                    ((root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(Socks_.COLOR), filterSocks.getColor()));
            list.add(colorSpec);
        }
        if (filterSocks.getCottonPart() != null) {
            Operation operation = Operation.getByString(filterSocks.getOperation());
            Specification<Socks> cottonPartSpec = getSpecificationByOperation(operation, filterSocks.getCottonPart());
            list.add(cottonPartSpec);
        }
        return list;
    }

    /**
     * Method creates specification from String operation and cottonPart
     *
     * @param operation  comparison operation
     * @param cottonPart comparison value
     * @return specification containing given parameters
     */
    private Specification<Socks> getSpecificationByOperation(Operation operation, Integer cottonPart) {
        return switch (operation) {
            case EQUAL -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(Socks_.COTTON_PART), cottonPart);
            case LESS_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lt(root.get(Socks_.COTTON_PART), cottonPart);
            case MORE_THAN -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.gt(root.get(Socks_.COTTON_PART), cottonPart);
        };
    }
}
