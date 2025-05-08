import api from '@/config/api.js'

export async function addNewItem(name, type_id, unit_id, storage_id, amount, expiry_date) {
  try {
    const response = await api.post(
      '/items/create-item-instance',
      {
        name: name,
        description: 'test',
        typeId: type_id,
        unitId: unit_id,
        storageId: storage_id,
        amount: amount,
        expiryDate: expiry_date },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )
    return response.data
  } catch (error) {
    console.error('Add new item error:', error)
    throw error
  }
}

export async function getAllFromType(storageId, typeId) {
  try {
    const response = await api.get(
      `/items/storage/${storageId}/item-instances`, {
        headers: {
          'Content-Type': 'application/json'
        },
        params: {
          typeId: typeId
        }
      }
    )
    return response.data
  } catch (error) {
    console.error('Get all type instances error:', error)
    throw error
  }
}

export async function deleteSelected(selectedIds) {
  try {
    const response = await api.delete(
      '/items/item-instances', {
        headers: {
          'Content-Type': 'application/json'
        },
        data: {
          instances: selectedIds
        }
      }
    )
    return response.data
  } catch (error) {
    console.error('Delete selected items error:', error)
    throw error
  }
}

export async function getAllItems() {
  try {
    const response = await api.get('/items', {
        headers: {
          'Content-Type': 'application/json'
        },
      }
    )
    return response.data
  } catch (error) {
    console.error('Get all items error:', error)
    throw error
  }
}

export async function edit(itemId, amount, expiryDate) {
  try {
    const response = await api.post(`/items/item-instances/${itemId}/edit`,
      {
        amount: amount,
        expiryDate: expiryDate },
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )
    return response.data
  } catch (error) {
    console.error('Edit item error:', error)
    throw error
  }
}
