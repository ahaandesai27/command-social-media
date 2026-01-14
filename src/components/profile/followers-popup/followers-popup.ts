import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FollowService } from '../../../services/user/follow.service';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../models/User';

export interface FollowersPopupData {
  username: string;
  type: 'followers' | 'following';
}

@Component({
  selector: 'app-followers-popup',
  imports: [CommonModule],
  templateUrl: './followers-popup.html',
  styleUrl: './followers-popup.css',
  standalone: true
})
export class FollowersPopup implements OnInit {
  @Input() data: FollowersPopupData | null = null;
  @Output() close = new EventEmitter<void>();

  public users: User[] = [];
  public loading: boolean = false;
  public error: string | null = null;

  constructor(
    private followService: FollowService,
    private userService: UserService
  ) {}

  ngOnInit() {
    if (this.data) {
      this.loadUsers();
    }
  }

  private loadUsers() {
    if (!this.data) return;

    this.loading = true;
    this.error = null;

    const request = this.data.type === 'followers'
      ? this.followService.getFollowers(this.data.username)
      : this.followService.getFollowing(this.data.username);

    request.subscribe({
      next: (usernames: string[]) => {
        if (usernames.length === 0) {
          this.users = [];
          this.loading = false;
          return;
        }

        // Fetch user details for each username
        const userRequests = usernames.map(username => 
          this.userService.getUserByUsername(username)
        );

        // Wait for all user requests to complete
        let completedRequests = 0;
        this.users = [];

        userRequests.forEach((userRequest, index) => {
          userRequest.subscribe({
            next: (user: User) => {
              this.users[index] = user;
              completedRequests++;
              
              if (completedRequests === userRequests.length) {
                // Remove any undefined entries and sort
                this.users = this.users.filter(user => user !== undefined);
                this.loading = false;
              }
            },
            error: (error) => {
              console.error(`Failed to load user ${usernames[index]}:`, error);
              completedRequests++;
              
              if (completedRequests === userRequests.length) {
                this.users = this.users.filter(user => user !== undefined);
                this.loading = false;
              }
            }
          });
        });
      },
      error: (error) => {
        console.error('Failed to load users:', error);
        this.error = 'Failed to load users. Please try again.';
        this.loading = false;
      }
    });
  }

  onClose() {
    this.close.emit();
  }

  onBackdropClick(event: Event) {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }
}