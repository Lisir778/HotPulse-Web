import { request } from './http'

export function listFavorites(userId) {
  return request(`/favorite?userId=${encodeURIComponent(userId)}`)
}

export function addFavorite({ userId, newsId, type }) {
  return request('/favorite', {
    method: 'POST',
    body: { userId, newsId, type },
  })
}

export function removeFavorite(id) {
  return request(`/favorite/${id}`, { method: 'DELETE' })
}
