import { Component, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from '../../services/post.service';
import { JwtService } from '../../services/auth/jwt.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-create-post',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-post.html',
  styleUrl: './create-post.css',
  standalone: true,
})
export class CreatePost {
  postForm: FormGroup;
  isSubmitting = false;
  submitError: string | null = null;
  currentUsername: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
    private jwtService: JwtService,
    private router: Router
  ) {
    this.currentUsername = this.jwtService.getUsername();
    
    this.postForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(2000)]]
    });
  }

  onSubmit() {
    if (this.postForm.invalid || this.isSubmitting || !this.currentUsername) {
      this.markFormGroupTouched(); // marks all fields as touched, so validation errors are shown 
      // invalid, isSubmitting, etc are just safeguards so code isnt executed 
      return;
    }

    this.isSubmitting = true;
    this.submitError = null;

    const { title, description } = this.postForm.value;

    this.postService.createPost(title, description, this.currentUsername)
      .subscribe({
        next: (post) => {
          console.log('Post created successfully:', post);
          this.router.navigate(['/posts']); // Redirect to posts page
        },
        error: (error) => {
          console.error('Error creating post:', error);
          this.submitError = 'Failed to create post. Please try again.';
          this.isSubmitting = false;
        }
      });
  }

  private markFormGroupTouched() {
    Object.keys(this.postForm.controls).forEach(key => {
      this.postForm.get(key)?.markAsTouched();
    });
  }

  onCancel() {}

  // Helper methods for form template template
  get title() { return this.postForm.get('title'); }
  get description() { return this.postForm.get('description'); }

  getTitleError(): string {
    const control = this.title;
    if (control?.errors && control.touched) {
      if (control.errors['required']) return 'Title is required';
      if (control.errors['minlength']) return 'Title must be at least 3 characters';
      if (control.errors['maxlength']) return 'Title cannot exceed 100 characters';
    }
    return '';
  }

  getDescriptionError(): string {
    const control = this.description;
    if (control?.errors && control.touched) {
      if (control.errors['required']) return 'Description is required';
      if (control.errors['minlength']) return 'Description must be at least 10 characters';
      if (control.errors['maxlength']) return 'Description cannot exceed 2000 characters';
    }
    return '';
  }
}