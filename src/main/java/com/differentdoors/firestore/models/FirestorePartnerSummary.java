package com.differentdoors.firestore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * De lijstweergave van een partner: de velden die overzichten en keuzelijsten nodig
 * hebben, zonder de gebruikers en overige metadata. Wordt niet per partner gelezen maar
 * als onderdeel van {@link FirestorePartnerIndex}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FirestorePartnerSummary {
    private String documentId;
    private String display_name;
    private String logo_url;
    private FirestorePartnerMetadata metadata;
    private Integer user_count;

    public static FirestorePartnerSummary of(FirestorePartner partner) {
        FirestorePartnerMetadata metadata = new FirestorePartnerMetadata();
        if (partner.getMetadata() != null) {
            metadata.setEnvironment(partner.getMetadata().getEnvironment());
        }
        return new FirestorePartnerSummary(
                partner.getDocumentId(),
                partner.getDisplay_name(),
                partner.getLogo_url(),
                metadata,
                partner.getUsers() == null ? 0 : partner.getUsers().size()
        );
    }
}
