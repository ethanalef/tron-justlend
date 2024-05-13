package org.tron.justlend.justlendapiserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "chaser_progress")
public class ChaserProgress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String event;
  @Column(name = "processed_height")
  private BigInteger processedHeight;
}