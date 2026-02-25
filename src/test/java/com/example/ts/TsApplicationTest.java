package com.example.ts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TsApplicationTest {

  @Test
  void mainShouldRun() {
    assertDoesNotThrow(() ->
        TsApplication.main(new String[]{})
    );
  }
}