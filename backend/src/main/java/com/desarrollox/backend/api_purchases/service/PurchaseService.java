package com.desarrollox.backend.api_purchases.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.desarrollox.backend.api_payment.exception.PaymentNotFoundException;
import com.desarrollox.backend.api_payment.model.Payment;
import com.desarrollox.backend.api_payment.repository.IPaymentRepository;
import com.desarrollox.backend.api_purchases.controller.dto.PurchaseDto;
import com.desarrollox.backend.api_purchases.exception.PaymentAlreadyUsedException;
import com.desarrollox.backend.api_purchases.exception.PurchaseAlreadyRegisteredException;
import com.desarrollox.backend.api_purchases.exception.PurchaseNotFoundException;
import com.desarrollox.backend.api_purchases.model.Purchase;
import com.desarrollox.backend.api_purchases.repository.IPurchaseRepository;
import com.desarrollox.backend.api_register.exception.UserNotFoundException;
import com.desarrollox.backend.api_register.model.User;
import com.desarrollox.backend.api_register.repository.IRegisterRepository;
import com.desarrollox.backend.api_videos.exception.VideoNotFoundException;
import com.desarrollox.backend.api_videos.model.Video;
import com.desarrollox.backend.api_videos.repository.IVideoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService implements IPurchaseService{
    private final IPurchaseRepository purchaseRepository;
    private final IRegisterRepository registerRepository;
    private final IPaymentRepository paymentRepository;
    private final IVideoRepository videoRepository;

    @Override
    public Purchase create(PurchaseDto purchase) {
        Optional<User> user = registerRepository.findById(purchase.getBuyerUserId());

        if (user.isEmpty()) {
            throw new UserNotFoundException("El usuario con id " + purchase.getBuyerUserId() + " no fue encontrado");
        }
        Optional<Payment> payment = paymentRepository.findById(purchase.getPaymentId());

        if (payment.isEmpty()) {
            throw new PaymentNotFoundException("El pago con id " + purchase.getPaymentId() + " no fue encontrado");
        }

        Optional<Video> video = videoRepository.findById(purchase.getVideoId());

        if (video.isEmpty()) {
            throw new VideoNotFoundException("El video con id " + purchase.getVideoId() + " no fue encontrado");
        }

        if(purchaseRepository.hasUserPurchasedVideo(user.get().getId(), video.get().getId()) > 0){
            throw new PurchaseAlreadyRegisteredException("El usuario con id " + user.get().getId() + " ya ha comprado el video con id " + video.get().getId());
        }

        if (purchaseRepository.countByPaymentId(payment.get().getId()) > 0) {
            throw new PaymentAlreadyUsedException(
                "El pago con id " + payment.get().getId() + " ya está asociado a una compra."
            );
        }

        Purchase purchaseEntity = Purchase.builder()
                .user(user.get())
                .payment(payment.get())
                .video(video.get())
                .build();

        return purchaseRepository.save(purchaseEntity);
    }

    @Override
    public Optional<Purchase> softDeleteCliente(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);

        if (purchase.isEmpty()) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no fue encontrada");
        }

        int rows = purchaseRepository.softDeleteCliente(id);

        if (rows == 0) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no pudo desactivarse");
        }

        purchase.get().setStatusPurchaseCliente(false);
        return purchase;
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        if (purchaseRepository.existsById(id)) {
            return Optional.of(purchaseRepository.findById(id).get());
        }else{
            throw new PurchaseNotFoundException("La compra con id: "+id+" no fue encontrada");
        }
    }

    @Override
    public List<Purchase> findByBuyerUserId(Long buyerId) {
        if(!registerRepository.existsById(buyerId)){
            throw new UserNotFoundException("El usuario con id " + buyerId + " no fue encontrado");
        }
        return purchaseRepository.findByUser_IdAndStatusPurchaseClienteIsTrue(buyerId);
    }

    @Override
    public List<Purchase> findAll() {
        return purchaseRepository.findByStatusPurchaseAdminIsTrue();
    }

    @Override
    public Optional<Purchase> softDeleteAdmin(Long id) {
        Optional<Purchase> purchase = purchaseRepository.findById(id);

        if (purchase.isEmpty()) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no fue encontrada");
        }

        int rows = purchaseRepository.softDeleteAdmin(id);

        if (rows == 0) {
            throw new PurchaseNotFoundException("La compra con id " + id + " no pudo desactivarse");
        }

        purchase.get().setStatusPurchaseAdmin(false);
        return purchase;
    }

    @Override
    public Void clear() {
        purchaseRepository.softDeleteAll();
        return null;
    }
}
