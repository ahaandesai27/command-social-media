import { Project } from "./Project"

export type User = {
  username: string
  name: string
  title: string
  about: string
  location: string
  joinDate: string
  followers: number
  following: number
  avatar: string
  techStack: string[]
  projects: Project[]
}
