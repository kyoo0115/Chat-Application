package project.realtimechatapplication.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "file")
@Entity
@Getter
@Setter
public class FileEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String fileType;

  @Lob
  @Column(nullable = false)
  private byte[] data;

  @ManyToOne
  @JoinColumn(name = "message_id", nullable = false)
  private MessageEntity message;

}
