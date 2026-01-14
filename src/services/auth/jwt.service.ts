import { Injectable } from '@angular/core'
import { jwtDecode } from 'jwt-decode'

type JwtPayload = {
  sub?: string
  iat?: number
  exp?: number
  userId?: number
}

@Injectable({ providedIn: 'root' })
export class JwtService {
  private getToken(): string | null {
    return localStorage.getItem('token')
  }

  decode(): JwtPayload | null | undefined {
    const token     = this.getToken()
        if (!token) return null
    
        try {
      const payload = jwtDecode<JwtPayload>(token)
      console.log("decoded payload", payload)
      return payload
    } catch {
      return null
    }
  }

  getUsername(): string | null {
    return this.decode()?.sub ?? null
  }

  getId(): number | null {
    return this.decode()?.userId ?? null;
  }

  isExpired(): boolean {
    const exp = this.decode()?.exp
    if (!exp) return true
    return Date.now() >= exp * 1000
  }
}
