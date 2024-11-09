export interface PageRequest {
  page: number;
  size: number;
  sort?: string;
  search?: string;
  category?: number;
  status?: string;
}
