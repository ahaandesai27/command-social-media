import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment as env } from '../env/env';
import { Post as PostData } from '../models/Post';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private readonly API_URL = `${env.apiBaseUrl}/search`;

  constructor(private http: HttpClient) {}

  searchUsers(query: string, limit: number = 10): Observable<string[]> {
    console.log(limit)
    const params = new HttpParams()
      .set('query', query)
      .set('size', limit.toString());
    
    return this.http.get<string[]>(`${this.API_URL}/users`, { params });
  }

  searchPosts(query: string, page: number = 0, size: number = 10): Observable<PostData[]> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<PostData[]>(`${this.API_URL}/posts`, { params });
  }
}