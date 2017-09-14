package id.unware.poken.ui.seller.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.seller.model.ISellerPageModel;
import id.unware.poken.ui.seller.view.ISellerPageView;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public class SellerPagePresenter implements ISellerPagePresenter, ISellerPageModelPresenter {

    private final ISellerPageModel model;
    private final ISellerPageView view;

    /** State will be ON if user explicitly press subscription button */
    private boolean isToggleSubscription = false;

    public SellerPagePresenter(ISellerPageModel model, ISellerPageView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getSellerPageProductData(long sellerId) {
        model.requestSellerData(this, sellerId);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void startDetailScreen(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void subscribeOnSeller(long sellerId, boolean isSubscribe) {

        // Indicate that user tend to toggle Subscription
        this.isToggleSubscription = true;

        model.requestSubscription(this, sellerId, !isSubscribe /* Default FALSE, invert.*/);
    }

    @Override
    public void onSellerPageContentResponse(ArrayList<Product> products) {
        if (view.isActivityFinishing()) return;

        view.pupolateSellerProductList(products);
    }

    @Override
    public void setupSellerInfo(Seller seller) {
        if (view.isActivityFinishing()) return;

        view.showSellerInfo(seller);

        // Update view that logged in user has subscribe
        this.view.showSubscriptionStatus(seller.is_subscribed);
    }

    @Override
    public void onSuscriptionSuccess(boolean isSubscribe) {
        if (view.isActivityFinishing()) return;

        // Update view that logged in user has subscribe
        this.view.showSubscriptionStatus(isSubscribe);

        // Show message on subsciption toggle
        this.view.showSubscriptionStatusMessage(isSubscribe);
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }
}
