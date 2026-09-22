package com.differentdoors.firestore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Eén document met de lijstweergave van alle partners. Firestore rekent per gelezen
 * document, dus de partnerlijst kost hiermee één read in plaats van één per partner.
 * Het is geen cache: elke create, update en delete van een partner schrijft dit
 * document in dezelfde aanvraag opnieuw, zodat het altijd de actuele stand bevat.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirestorePartnerIndex {
    private List<FirestorePartnerSummary> partners;
}
