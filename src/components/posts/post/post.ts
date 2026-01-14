import { Component, Input, OnDestroy } from '@angular/core';
import { Comment } from './comment/comment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Post as PostData } from '../../../models/Post';
import { Comment as CommentData } from '../../../models/Comment';
import { CommentService } from '../../../services/comment.service';
import { PostService } from '../../../services/post.service';
import { JwtService } from '../../../services/auth/jwt.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-post',
  imports: [CommonModule, FormsModule, Comment],
  templateUrl: './post.html',
  styleUrl: './post.css',
  standalone: true,
})
export class Post implements OnDestroy {
  @Input() postData!: PostData;
  public comments: CommentData[] = [];

  // Comment pagination variables
  private commentPage = 0;
  private commentSize = 5;
  public loadingComments = false;
  public hasMoreComments = true;
  private commentSubscription?: Subscription;

  commentsLoaded = false;
  currentUserId: number | null = null;
  currentUsername: string | null = null;
  showComments: boolean = false;
  newComment: string = '';
  DESCRIPTION_LIMIT = 200
  showFullDescription = false;

  constructor ( private postService: PostService,
                private commentService: CommentService,
                private jwtService: JwtService) {
                  this.currentUserId = jwtService.getId();
                  this.currentUsername = jwtService.getUsername();
                }

  ngOnDestroy() {
    this.commentSubscription?.unsubscribe();
  }

  onLikePost() {
    if (this.currentUserId == null) return;
    
    const prevLiked = this.postData.liked;
    const prevLikes = this.postData.likes;

    this.postData.liked = !prevLiked;
    this.postData.likes += this.postData.liked ? 1 : -1;

    this.postService.likePost(this.currentUserId, this.postData.id).subscribe({
      error: () => {
        this.postData.liked = prevLiked;
        this.postData.likes = prevLikes;
      }
    });
  }

  toggleDescription() {
    this.showFullDescription = !this.showFullDescription;
  }

  toggleComments() {
    this.showComments = !this.showComments;
    if (this.showComments && !this.commentsLoaded) {
        this.loadMoreComments();
    }
  }

  loadMoreComments() {
    if (this.loadingComments || !this.hasMoreComments) {
      return;
    }

    this.loadingComments = true;
    this.commentSubscription = this.commentService.getCommentsByPost(
      this.postData.id, 
      this.commentPage, 
      this.commentSize
    ).subscribe({
      next: (newComments) => {
        if (!newComments || newComments.length === 0) {
          this.hasMoreComments = false;
        } else {
          this.comments = [...this.comments, ...newComments];
          this.commentPage++;
        }
        this.commentsLoaded = true;
        this.loadingComments = false;
      },
      error: (error) => {
        console.error('Error loading comments:', error);
        this.loadingComments = false;
      }
    });
  }

  submitComment() {
    if (!this.newComment?.trim() || this.currentUsername == null) return;
    
    
    this.commentService.createComment(this.currentUsername, this.postData.id, this.newComment).subscribe({
      next: (newComment) => {
        this.comments.push(newComment);
        this.newComment = '';
      },
      error: (error) => {
        console.error('Failed to add comment:', error);
      }
    });
  }
}
