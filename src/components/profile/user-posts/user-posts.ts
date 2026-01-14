import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PostService } from '../../../services/post.service';
import { Post } from '../../../components/posts/post/post';
import { Post as PostData } from '../../../models/Post';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-posts',
  imports: [CommonModule, Post],
  templateUrl: './user-posts.html',
  styleUrl: './user-posts.css',
  standalone: true
})
export class UserPosts implements OnInit, OnDestroy {
  @Input() username!: string;
  
  public posts: PostData[] = [];
  public loading = false;
  public hasMorePosts = true;
  private page = 0;
  private size = 5;
  private subscription?: Subscription;

  constructor(private postService: PostService) {}

  ngOnInit() {
    if (this.username) {
      this.loadUserPosts();
    }
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private loadUserPosts(reset = true) {
    if (this.loading) return;

    if (reset) {
      this.page = 0;
      this.posts = [];
      this.hasMorePosts = true;
    }

    this.loading = true;

    this.subscription = this.postService.getPostsByUser(this.username, this.page, this.size).subscribe({
      next: (newPosts) => {
        if (reset) {
          this.posts = newPosts;
        } else {
          this.posts = [...this.posts, ...newPosts];
        }
        
        this.hasMorePosts = newPosts.length === this.size;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading user posts:', error);
        this.loading = false;
        this.hasMorePosts = false;
      }
    });
  }

  public loadMorePosts() {
    if (!this.hasMorePosts || this.loading) return;
    
    this.page++;
    this.loadUserPosts(false);
  }

  public refresh() {
    this.loadUserPosts(true);
  }
}
