package com.atrilos.socksrest.service;

import com.atrilos.socksrest.exception.NegativeSocksAmountException;
import com.atrilos.socksrest.exception.NoParameterProvidedException;
import com.atrilos.socksrest.model.ChangeSocksDTO;
import com.atrilos.socksrest.model.FilterSocks;
import com.atrilos.socksrest.model.Socks;
import com.atrilos.socksrest.repository.SocksRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocksServiceTest {

    @InjectMocks
    private SocksService out;

    @Mock
    private SocksRepository socksRepository;

    @Test
    public void changeSocks_Income_Positive() {
        ChangeSocksDTO income = ChangeSocksDTO.builder()
                .color("black")
                .cottonPart(30)
                .quantity(5L)
                .build();
        Socks inBD = Socks.builder()
                .color(income.getColor())
                .cottonPart(income.getCottonPart())
                .quantity(10L)
                .build();
        Socks expected = Socks.builder()
                .color(income.getColor())
                .cottonPart(income.getCottonPart())
                .quantity(15L)
                .build();


        when(socksRepository.findByColorAndCottonPart(income.getColor(), income.getCottonPart()))
                .thenReturn(Optional.of(inBD));
        when(socksRepository.save(any(Socks.class)))
                .then(returnsFirstArg());

        assertThat(out.changeSocks(income))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void changeSocks_Income_EmptyDB_Positive() {
        ChangeSocksDTO income = ChangeSocksDTO.builder()
                .color("black")
                .cottonPart(30)
                .quantity(5L)
                .build();
        Socks expected = Socks.builder()
                .color(income.getColor())
                .cottonPart(income.getCottonPart())
                .quantity(5L)
                .build();


        when(socksRepository.findByColorAndCottonPart(income.getColor(), income.getCottonPart()))
                .thenReturn(Optional.empty());
        when(socksRepository.save(any(Socks.class)))
                .then(returnsFirstArg());

        assertThat(out.changeSocks(income))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void changeSocks_Outcome_Negative() {
        ChangeSocksDTO income = ChangeSocksDTO.builder()
                .color("black")
                .cottonPart(30)
                .quantity(-5)
                .build();

        when(socksRepository.findByColorAndCottonPart(income.getColor(), income.getCottonPart()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> out.changeSocks(income))
                .isInstanceOf(NegativeSocksAmountException.class);
    }

    @Test
    public void changeSocks_Outcome_Positive() {
        ChangeSocksDTO income = ChangeSocksDTO.builder()
                .color("black")
                .cottonPart(30)
                .quantity(-5L)
                .build();
        Socks inBD = Socks.builder()
                .color(income.getColor())
                .cottonPart(income.getCottonPart())
                .quantity(5L)
                .build();
        Socks expected = Socks.builder()
                .color(income.getColor())
                .cottonPart(income.getCottonPart())
                .quantity(0L)
                .build();


        when(socksRepository.findByColorAndCottonPart(income.getColor(), income.getCottonPart()))
                .thenReturn(Optional.of(inBD));
        when(socksRepository.save(any(Socks.class)))
                .then(returnsFirstArg());

        assertThat(out.changeSocks(income))
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void getSocksFiltered_Negative() {
        FilterSocks filter = FilterSocks.builder()
                .build();

        assertThatThrownBy(() -> out.getSocksFiltered(filter))
                .isInstanceOf(NoParameterProvidedException.class);
    }
}