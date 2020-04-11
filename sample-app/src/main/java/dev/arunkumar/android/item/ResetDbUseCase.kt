package dev.arunkumar.android.item

import io.reactivex.Completable
import javax.inject.Inject

class ResetDbUseCase
@Inject
constructor(
  private val itemsRepository: ItemsRepository
) {

  fun build(): Completable {
    return itemsRepository.clear().andThen(itemsRepository.addItemsIfEmpty())
  }
}