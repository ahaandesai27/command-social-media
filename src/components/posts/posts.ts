import { Component, ElementRef, HostListener, ViewChild, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Post } from './post/post';
import { Post as PostModel } from '../../models/Post';
import { PostService } from '../../services/post.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-posts',
  imports: [Post, CommonModule],
  templateUrl: './posts.html',
  styleUrl: './posts.css',
  standalone: true,
})
export class Posts implements OnDestroy {
    public posts: PostModel[] = [];

    // Pagniation variables 
    private page = 0;
    private size = 10;
    public isLoading = false;
    public hasMorePosts = true;
    private loadingSubscription?: Subscription;
    private scrollThreshold = 200; // pixels from bottom to trigger load

    constructor(private postService: PostService, private router: Router) {}

    ngOnInit() {
        this.loadMore();      // works as initial loading too 
    }

    ngOnDestroy() {
        this.loadingSubscription?.unsubscribe();
    }

    @HostListener('window:scroll', [])    // [] denotes event properties passed 
    onScroll() {
        if (this.isLoading || !this.hasMorePosts) {
            return;
        }

        const windowHeight = window.innerHeight;
        const documentHeight = document.documentElement.scrollHeight;
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        // Check if user has scrolled near the bottom
        if (windowHeight + scrollTop >= documentHeight - this.scrollThreshold) {
            this.loadMore();
        }
    }

    loadMore() {
        if (this.isLoading || !this.hasMorePosts) {
            return;
        }

        this.isLoading = true;
        this.loadingSubscription = this.postService.getPosts(this.page, this.size).subscribe({
            next: (posts) => {
                if (!posts || posts.length === 0) {
                    this.hasMorePosts = false;
                } else {
                    this.posts = [...this.posts, ...posts];
                    this.page++;
                }
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading posts:', error);
                this.isLoading = false;
            }
        });
    }

    navigateToCreatePost() {
        this.router.navigate(['/posts/create']);
    }
}
