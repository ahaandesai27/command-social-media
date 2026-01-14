import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SearchService } from '../../services/search.service';
import { Post as PostComponent } from '../posts/post/post';
import { Post as PostData } from '../../models/Post';
import { debounceTime, Subject, switchMap } from 'rxjs';

@Component({
  selector: 'app-search',
  imports: [CommonModule, FormsModule, PostComponent],
  templateUrl: './search.html',
  styleUrl: './search.css',
  standalone: true
})
export class Search implements OnInit, OnDestroy {
  searchQuery: string = '';
  activeTab: 'users' | 'posts' = 'users';
  
  // Results
  userResults: string[] = [];
  postResults: PostData[] = [];
  
  // Loading states
  loadingUsers: boolean = false;
  loadingPosts: boolean = false;
  
  // Pagination for posts
  currentPage: number = 0;
  pageSize: number = 10;
  hasMorePosts: boolean = true;
  
  // Search subject for debouncing
  private searchSubject = new Subject<string>();

  constructor(
    private searchService: SearchService,
    private router: Router
  ) {}

  ngOnInit() {
    // Setup debounced search
    this.searchSubject
      .pipe(      
        debounceTime(300),                  // waits for 300 ms and then calls the function
        switchMap((query: string) => {      // a switch case basically 
          if (!query.trim()) {
            this.clearResults();
            return [];
          }
          
          if (this.activeTab === 'users') {
            this.loadingUsers = true;
            return this.searchService.searchUsers(query);
          } else {
            this.loadingPosts = true;
            this.currentPage = 0;
            return this.searchService.searchPosts(query, 0, this.pageSize);
          }
          
        })
      )
      .subscribe({
        next: (results: any) => {
          if (this.activeTab === 'users') {
            this.userResults = results;
            this.loadingUsers = false;
          } else {
            this.postResults = results;
            this.hasMorePosts = results.length === this.pageSize;
            this.loadingPosts = false;
          }
        },
        error: (error) => {
          console.error('Search error:', error);
          this.loadingUsers = false;
          this.loadingPosts = false;
        }
      });
  }

  onSearchInput(query: string) {
    this.searchQuery = query;
    this.searchSubject.next(query);
  }

  switchTab(tab: 'users' | 'posts') {
    if (this.activeTab === tab) return;
    
    this.activeTab = tab;
    this.clearResults();
    
    if (this.searchQuery.trim()) {
      this.searchSubject.next(this.searchQuery);
    }
  }

  clearResults() {
    this.userResults = [];
    this.postResults = [];
    this.currentPage = 0;
    this.hasMorePosts = true;
  }

  loadMorePosts() {
    // simple pagination
    if (!this.hasMorePosts || this.loadingPosts || !this.searchQuery.trim()) return;
    
    this.loadingPosts = true;
    this.currentPage++;
    
    this.searchService.searchPosts(this.searchQuery, this.currentPage, this.pageSize)
      .subscribe({
        next: (results) => {
          this.postResults = [...this.postResults, ...results];
          this.hasMorePosts = results.length === this.pageSize;
          this.loadingPosts = false;
        },
        error: (error) => {
          console.error('Load more posts error:', error);
          this.loadingPosts = false;
          this.currentPage--; // Revert page increment on error
        }
      });
  }

  navigateToProfile(username: string) {
    this.router.navigate(['/profile', username]);
  }

  ngOnDestroy() {
    this.searchSubject.complete();
  }
}
