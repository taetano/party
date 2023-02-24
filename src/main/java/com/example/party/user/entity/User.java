package com.example.party.user.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.party.application.entity.Application;
import com.example.party.chat.entity.EnrolledChatRoom;
import com.example.party.global.common.TimeStamped;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.dto.ProfilesRequest;
import com.example.party.user.dto.SignupRequest;
import com.example.party.user.type.Status;
import com.example.party.user.type.UserRole;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends TimeStamped implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @Column(name = "nickname", nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(name = "phone_number", nullable = false, length = 13, columnDefinition = "CHAR(13)")
    private String phoneNum;

    // enum
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 12)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE', 'SUSPENDED', 'DORMANT')")
    private Status status;

    // 연관관계
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "profiles_id", unique = true, referencedColumnName = "id")
    private Profile profile;
    @OneToMany(mappedBy = "user")
    private List<Application> applies;
    @OneToMany(mappedBy = "user")
    private List<PartyPost> partyPosts;
    @OneToMany(mappedBy = "user")
    private List<EnrolledChatRoom> enrolledChatRoom;
    @ManyToMany
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<PartyPost> likePartyPosts;

    public String getProfileImg() {
        return this.profile.getImg();
    }

    public String getComment() {
        return this.profile.getComment();
    }

    public int getNoShowCnt() {
        return this.profile.getNoShowCnt();
    }

    public int getParticipationCnt() {
        return this.profile.getParticipationCnt();
    }

    public User(SignupRequest signupRequest, String password, Profile profile) {
        this.email = signupRequest.getEmail();
        this.password = password;
        this.nickname = signupRequest.getNickname();
        this.phoneNum = signupRequest.getPhoneNum();
        this.role = UserRole.ROLE_USER;
        this.status = Status.ACTIVE;
        this.profile = profile;
    }

    public void DormantState() {
        this.status = Status.DORMANT;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        for (UserRole eachRole : UserRole.values()) {
            authorities.add(new SimpleGrantedAuthority(eachRole.name()));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void updateProfile(ProfilesRequest profilesRequest) {
        this.nickname = profilesRequest.getNickname();
        this.phoneNum = profilesRequest.getPhoneNum();
    }

    public void increaseParticipationCnt() {
        this.profile.increaseParticipationCnt();
    }

    public void setId(Long id) { // 테스트를 위한 추가
        this.id = id;
    }

    //작성한 참가신청 목록 추가
    public void addApplication(Application application) {
        this.applies.add(application);
    }


    //오브잭트 클레스에서 제공하는
    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }
}
