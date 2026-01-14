import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from '../../models/User';
import { UserService } from '../../services/user/user.service';
import { JwtService } from '../../services/auth/jwt.service';
import { ActivatedRoute } from '@angular/router';
import { FollowService, FollowDto } from '../../services/user/follow.service';
import { UserPosts } from './user-posts/user-posts';
import { FollowersPopup, FollowersPopupData } from './followers-popup/followers-popup';


@Component({
  selector: 'app-profile',
  imports: [CommonModule, UserPosts, FollowersPopup],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
  standalone: true
})
export class Profile {
  public user: User | null = null;            // IMP: public declaration if used in HTML
  public isOwnProfile: boolean = false;
  public isFollowing: boolean = false;
  public loadingFollow: boolean = false;
  public profileNotFound: boolean = false;
  public showFollowersPopup: boolean = false;
  public followersPopupData: FollowersPopupData | null = null;
  private currentUsername: string | null = null;

  constructor(private userService: UserService,
    private jwtService: JwtService,
    private route: ActivatedRoute,
    private followService: FollowService
  ) {
    this.currentUsername = this.jwtService.getUsername();
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      // paramMap is an Observable returned by angular router 
      // params is the argument (paramMap) passed to the callback function

      const routeUsername = params.get('username');
      const username = routeUsername ?? this.currentUsername;
      if (!username) return;    // can show some page 

      this.isOwnProfile = username === this.currentUsername;

      this.userService.getUserByUsername(username).subscribe({
        next: user => {
          this.user = user
          this.profileNotFound = false

          if (!this.isOwnProfile && this.currentUsername) {
            this.checkFollowStatus()
          }

          if (!this.user.avatar) {
            this.user.avatar = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg";
          }
        },
        error: err => {
          if (err.status === 404 || err.status === 500) {
            this.profileNotFound = true
          }
        }
      })

    })
  }

  private checkFollowStatus() {
    if (!this.currentUsername) return;

    this.followService.getFollowing(this.currentUsername).subscribe({
      next: (following) => {
        this.isFollowing = following.includes(this.user?.username || '');
      },
      error: (error) => {
        console.error('Error checking follow status:', error);
      }
    });
  }

  onToggleFollow() {
    if (!this.user || !this.currentUsername || this.isOwnProfile || this.loadingFollow) {
      return;
    }

    this.loadingFollow = true;
    const followDto: FollowDto = {
      follower: this.currentUsername,
      followee: this.user.username
    };

    const operation = this.isFollowing
      ? this.followService.unfollowUser(followDto)
      : this.followService.followUser(followDto);

    operation.subscribe({
      next: () => {
        this.isFollowing = !this.isFollowing;
        // Update the followers count optimistically
        if (this.user) {
          this.user.followers += this.isFollowing ? 1 : -1;
        }
        this.loadingFollow = false;
      },
      error: (error) => {
        console.error('Error toggling follow:', error);
        this.loadingFollow = false;
      }
    });
  }

  onShowFollowers() {
    if (!this.user) return;
    
    this.followersPopupData = {
      username: this.user.username,
      type: 'followers'
    };
    this.showFollowersPopup = true;
  }

  onShowFollowing() {
    if (!this.user) return;
    
    this.followersPopupData = {
      username: this.user.username,
      type: 'following'
    };
    this.showFollowersPopup = true;
  }

  onCloseFollowersPopup() {
    this.showFollowersPopup = false;
    this.followersPopupData = null;
  }

}
