package com.test.oddschecker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "odds")
public class Odds {

    private static final long serialVersionUID = -1723798766434132067L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer oddsId;
    @NotNull(message = "Should contain only numbers")
    private Integer betId;
    @NotNull
    @ApiModelProperty(notes = "ID of user who is offering the odds")
    private String userId;
    @NotNull
    @ApiModelProperty(notes = "example 1/10")
    private String odds;

    @JsonIgnore
    public Integer getOddsId() {
        return oddsId;
    }

    @JsonIgnore
    public void setOddsId(final Integer oddsId) {
        this.oddsId = oddsId;
    }

    @Override
    public String toString() {
        return "[Odds: "
                + " oddsId " + oddsId
                + " userId " + betId
                + " userId " + userId
                + " odds " + odds
                + " ]";
    }
}
