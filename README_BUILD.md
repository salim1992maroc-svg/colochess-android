# ColoChess build configuration

The Android API root is no longer hard-coded in Java.

Set this Gradle property on the build machine:

    COLOC_API_BASE_URL=https://your-api-domain.example/

For example in the user's Gradle properties file (`~/.gradle/gradle.properties`):

    COLOC_API_BASE_URL=https://api.example.com/

The value must end up with HTTPS in production. The source repository intentionally
contains only a placeholder so an API host is not accidentally published in source.
