package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "show_alarm")
public class ShowAlarm extends BaseEntity {

    @Column(name = "user_fcm_token", nullable = false)
    private String userFcmToken;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    @Builder
    private ShowAlarm(String userFcmToken, String title, String content, boolean checked) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.content = content;
        this.checked = checked;
    }

    public void checked() {
        this.checked = true;
    }
}
