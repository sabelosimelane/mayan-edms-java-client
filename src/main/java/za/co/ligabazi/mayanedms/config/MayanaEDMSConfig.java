package za.co.ligabazi.mayanedms.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@AllArgsConstructor
public class MayanaEDMSConfig {
    @Getter
    @NonNull
    private final String host;

    @Getter
    @NonNull
    private final String username;

    @Getter
    @NonNull
    private final String password;

    @Getter
    @NonNull
    @Builder.Default
    private String apiVer = "v4";

}
