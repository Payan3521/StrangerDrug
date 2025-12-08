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
public class PurchaseService implements IPurchaseService {
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

        Optional<Video> video = videoRepository.findByS3Key(purchase.getVideoKey());

        if (video.isEmpty()) {
            throw new VideoNotFoundException("El video con id " + purchase.getVideoKey() + " no fue encontrado");
        }

        // Verificar si existe una compra ACTIVA del mismo video por el mismo usuario
        int activePurchases = purchaseRepository.hasUserPurchasedVideo(user.get().getId(), video.get().getId());

        if (activePurchases > 0) {
            throw new PurchaseAlreadyRegisteredException("El usuario con id " + user.get().getId()
                    + " ya tiene una compra activa del video con id " + video.get().getId());
        }

        // Si hay compras inactivas del mismo video, las dejamos como están (no hacemos
        // nada)
        // Esto permite que el usuario pueda recomprar un video que había eliminado de
        // su biblioteca

        if (purchaseRepository.countByPaymentId(payment.get().getId()) > 0) {
            throw new PaymentAlreadyUsedException(
                    "El pago con id " + payment.get().getId() + " ya está asociado a una compra activa.");
        }

        System.out.println("Creating purchase with amount: " + purchase.getAmount());

        Purchase purchaseEntity = Purchase.builder()
                .user(user.get())
                .payment(payment.get())
                .video(video.get())
                .amount(purchase.getAmount())
                .build();

        Purchase savedPurchase = purchaseRepository.save(purchaseEntity);
        System.out.println("Saved purchase with amount: " + savedPurchase.getAmount());

        return savedPurchase;
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
        } else {
            throw new PurchaseNotFoundException("La compra con id: " + id + " no fue encontrada");
        }
    }

    @Override
    public List<Purchase> findByBuyerUserId(Long buyerId) {
        if (!registerRepository.existsById(buyerId)) {
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
