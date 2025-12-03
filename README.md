# StrangerDrug

servicios
interceptors
pages
guards

3. api de pagos
4. conectar con el frontend (diagramar flujo entre backend y frontend)

1. terminos y condiciones
2. politica de privacidad
3. informacion de soporte al cliente
4. cláusula de facturación
5. verificacion de edad
6. precios claros
7. moneda clara
8. explicar facturacion y cobros
9. sitio web 100% funcional
10. ssl
11. dominio

# AwsConfig para probar local

    package com.desarrollox.backend.core.config;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
    import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
    import software.amazon.awssdk.regions.Region;
    import software.amazon.awssdk.services.s3.S3Client;
    import software.amazon.awssdk.services.s3.presigner.S3Presigner;


    @Configuration
    public class AWSConfig {

        @Value("${aws.access.key.id}")
        private String accessKey;
        
        @Value("${aws.secret.access.key}")
        private String secretKey;
        
        @Value("${aws.s3.region}")
        private String region;

        @Bean
        public S3Client amazonS3Client() {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

            return S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        }

        @Bean
        public S3Presigner s3Presigner() {
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

            return S3Presigner.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();
        }
        
    }