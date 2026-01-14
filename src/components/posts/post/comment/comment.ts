import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Comment as CommentData } from '../../../../models/Comment';

@Component({
  selector: 'app-comment',
  imports: [CommonModule],
  templateUrl: './comment.html',
  styleUrl: './comment.css',
  standalone: true,
})
export class Comment {
  @Input() comment!: CommentData;

  // logic later
}
