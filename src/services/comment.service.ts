import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../env/env";
import { Comment } from "../models/Comment";

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    private readonly API = `${environment.apiBaseUrl}/comments`

    constructor (private http: HttpClient) {}

    createComment(username: string, postId: number, content: string): Observable<Comment> {
      const object = {
        username,
        postId,
        content,
      }

      return this.http.post<Comment>(
        `${this.API}`, object
      )
    }

    getCommentsByPost(postId: number, page: number = 0, size: number = 10) {
      return this.http.get<Comment[]>(
        `${this.API}/post/${postId}?page=${page}&size=${size}`
      )
    }
}