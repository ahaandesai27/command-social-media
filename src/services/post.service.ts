import { Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { environment } from "../env/env";
import { Post } from "../models/Post";


@Injectable({
    providedIn: 'root'
})
export class PostService {
    private readonly API = `${environment.apiBaseUrl}/posts`

    constructor (private http: HttpClient) {}

    createPost(title: string, description: string, username: string): Observable<Post> {
      const object = {
        title,
        description,
        username
      }

      return this.http.post<Post>(
        `${this.API}`, object
      )
    }

    updatePost(title: string, description: string): Observable<Post> {
      const object = {
        title,
        description
      }

      return this.http.patch<Post>(
        `${this.API}`, object
      )
    }

    getPosts(page: number, size: number): Observable<Post[]>  {
      const url = `${this.API}?page=${page}&size=${size}`
      return this.http.get<Post[]>(url);
    }

    getPostsByUser(username: string, page: number, size: number): Observable<Post[]> {
        return this.http.get<Post[]>(
            `${this.API}/user/${username}?page=${page}&size=${size}`
        );
    }

    deletePost(postId: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${postId}`);
    }

    likePost(userId: number, postId: number) {
      return this.http.post(
        `${this.API}/${userId}/like/${postId}`,
        {},
        { responseType: 'text' }
      );
    }
}